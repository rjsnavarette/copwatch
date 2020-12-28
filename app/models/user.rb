class User < ApplicationRecord
  include BCrypt

  has_secure_token :auth_token
  has_secure_token :verification_token
  has_secure_token :password_reset_token

  # Notes
    # account_type: 0 - email, 1 - facebook, 2 - google

  # associations

  # scopes
  scope :verified, -> { where(is_verified: true) }

  # class methods
  def self.sign_up(data)
    logger.info "\n-- User : Model : sign_in --\n"
    user = User.new(data)

    if user.save
      UserMailer.with(user: user).verification_email.deliver_now

      { user: user.sign_up_format, status: 200 }
    else
      { error: user.get_error, status: 500 }
    end
  end

  def self.verify_email(email, token)
    user = User.find_by(email: email)

    if user.present? && user.verification_token == token
      if user.is_verified
        { error: "Email has already been verified.", status: 500 }
      elsif user.update(is_verified: true)
        { status: 200 }
      else
        { error: user.get_error, status: 500 }
      end
    else
      { error: "Invalid email or token.", status: 500 }
    end
  end

  def self.sign_in(data)
    logger.info "\n-- User : Model : sign_in --\ndata: #{data}"
    user = data[:email].present? ? User.find_by(email: data[:email]) : User.find_by(uuid: data[:uuid])

    case data[:account_type].to_i
    when 0
      # email
      User.email_sign_in(user, data[:password])
    else
      # facebook & google
      User.fb_google_sign_in(user, data.except(:password))
    end
  end

  def self.email_sign_in(user, password)
    logger.info "\n-- User : Model : email_sign_in --\n"
    if user.nil? || (user.present? && !user.valid_password?(password))
      { error: "Invalid Email or Password.", status: 500 }
    elsif !user.is_verified
      { error: "Please verify your email to sign in.", status: 500 }
    elsif user.regenerate_auth_token
      { user: user.sign_in_format, status: 200 }
    else
      { error: user.get_error, status: 500 }
    end
  end

  def self.fb_google_sign_in(user, data)
    logger.info "\n-- User : Model : fb_google_sign_in --\n"
    if user.nil?
      user              = User.new(data)
      user.is_verified  = true

      if user.save
        { user: user.sign_in_format, status: 200 }
      else
        { error: user.get_error, status: 500 }
      end
    else
      if user.regenerate_auth_token
        { user: user.sign_in_format, status: 200 }
      else
        { error: user.get_error, status: 500 }
      end
    end
  end

  def self.forgot_password(email)
    user = User.find_by(email: email)

    if user.present?
      if user.is_verified && user.regenerate_password_reset_token && user.regenerate_auth_token
        UserMailer.with(user: user).forgot_password_email.deliver_later

        { status: 200 }
      else
        { error: user.get_error.present? ? user.get_error : "Please verify your email first.", status: 500 }
      end
    else
      { error: "Invalid email.", status: 500 }
    end
  end

  def self.reset_password(token, password)
    user = User.find_by(password_reset_token: token)

    if user.present?
      if user.update({ password: password }) && user.regenerate_password_reset_token
        { status: 200 }
      else
        { error: user.get_error, status: 500 }
      end
    else
      { error: "Invalid token.", status: 500 }
    end
  end

  # validations
  validates :email, presence: true, email: true, uniqueness: true, if: -> { is_email_account? }
  validates :password, presence: true, length: { minimum: 8, maximum: 20 }, if: -> { (is_create? && is_email_account?) || (!is_create? && is_email_account? && is_password_changed?) }
  validates :first_name, presence: true
  validates :last_name, presence: true
  validates :uuid, presence: true, on: :create, if: -> { !is_email_account? }
  validates :auth_token, uniqueness: true
  validates :verification_token, uniqueness: true
  validates :password_reset_token, uniqueness: true

  # callbacks
  before_save :encrypt_password, if: -> { (is_create? && is_email_account?) || (!is_create? && is_email_account? && is_password_changed?) }

  # instance methods
  ## formatting methods
  def sign_up_format
    self.as_json(only: [:id, :first_name, :last_name, :email], methods: :photo_url)
  end

  def sign_in_format
    self.as_json(only: [:id, :first_name, :last_name, :auth_token], methods: :photo_url).merge({ email: self.email.to_s })
  end

  def photo_url
    self.photo.to_s
  end

  ## helper methods
  def get_error
    self.errors.full_messages.first
  end

  def valid_password?(pass)
    Password.new(self.password) == pass
  end

  def is_create?
    self.id.nil?
  end

  def is_email_account?
    self.account_type == 0
  end

  def is_password_changed?
    self.password_was != self.password
  end

  private

    def password_validity
      if self.password.nil?
        self.error.add(:password, "can't be blank.")
      elsif self.password.length < 8
        self.error.add(:password, "is too short (minimum is 8 characters)")
      elsif self.password.length > 20
        self.error.add(:password, "is too long (maximum is 20 characters)")
      end
    end

    def encrypt_password
      self.password = Password.create(self.password)
    end
end
