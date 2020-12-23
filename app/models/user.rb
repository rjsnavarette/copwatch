class User < ApplicationRecord
  include BCrypt

  has_secure_token :auth_token
  has_secure_token :verification_token

  # Notes
    # account_type: 0 - email, 1 - facebook, 2 - google

  # associations

  # scopes
  scope :verified, -> { where(is_verified: true) }

  # class methods
  def self.sign_up(data)
    logger.info "\n-- User : Model : sign_in --\n"
    user = User.new(data)

    if user.save && user.regenerate_auth_token && user.regenerate_verification_token 
      { user: user.sign_up_format, status: 200 }
    else
      { error: user.get_error, status: 500 }
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
      { error: "Please verify your account to sign in.", status: 500 }
    else
      { user: user.sign_in_format, status: 200 }
    end
  end

  def self.fb_google_sign_in(user, data)
    logger.info "\n-- User : Model : fb_google_sign_in --\n"
    if user.nil?
      user              = User.new(data)
      user.is_verified  = true

      if user.save && user.regenerate_auth_token
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

  # validations
  validates :email, presence: true, email: true, uniqueness: true, if: -> { is_email_account? }
  validates :password, presence: true, length: { minimum: 8, maximum: 20 }, on: :create, if: -> { is_email_account? }
  validates :first_name, presence: true
  validates :last_name, presence: true
  validates :uuid, presence: true, on: :create, if: -> { !is_email_account? }

  # callbacks
  before_create :encrypt_password

  # instance methods
  ## formatting methods
  def sign_up_format
    as_json(only: [:id, :first_name, :last_name, :email], methods: :photo_url)
  end

  def sign_in_format
    as_json(only: [:id, :first_name, :last_name, :auth_token], methods: :photo_url).merge({ email: email.to_s })
  end

  def photo_url
    photo.to_s
  end

  ## helper methods
  def get_error
    errors.full_messages.first
  end

  def valid_password?(pass)
    return Password.new(password) == pass
  end

  def is_email_account?
    account_type == 0
  end

  private

    def encrypt_password
      logger.info "\n-- User : Model : encrypt_password --\n"
      self.password = Password.create(self.password)
    end
end
