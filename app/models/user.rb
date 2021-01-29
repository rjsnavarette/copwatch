class User < ApplicationRecord
  include BCrypt

  has_secure_token :auth_token
  has_secure_token :verification_token
  has_secure_token :password_reset_token

  mount_uploader :photo, UserPhotoUploader

  # Notes
    # account_type: 0 - email, 1 - facebook, 2 - google, 3 - apple
    # storage_type: 0 - app storage (default), 1 - google cloud, 2 - icloud
    # mode_type: 0 - copwatch standard (default), 1 - dash cam / trip, 2 - clip / body cam

  # associations
  has_one   :preference, dependent: :destroy
  has_one   :permission, dependent: :destroy
  has_many  :feedbacks, dependent: :destroy
  has_many  :subscriptions, dependent: :destroy

  # deletegate
  delegate :is_recording_to_cloud, :is_dim_my_screen, :is_do_not_disturb,
    :is_recording_audio_video, :is_voice_activated, :is_rear_camera_selected,
    to: :preference, prefix: true

  # scopes
  scope :verified, -> { where(is_verified: true) }

  # class methods
  def self.sign_up(data, photo)
    logger.info "\n-- User : Model : sign_in --\n"
    user        = User.new(data)
    user.photo  = photo if photo.present?

    User.transaction do
      if user.save
        UserMailer.with(user: user).verification_email.deliver_now

        { user: user.sign_up_format, status: 200 }
      else
        { error: user.validation_error, status: 500 }
      end
    end
  end

  def self.verify_email(email, token)
    user = User.find_by(email: email)

    if user.present? && user.verification_token == token
      if user.is_verified
        { error: "Email has already been verified.", status: 500 }
      elsif user.update(is_verified: true)
        { auth_token: user.auth_token, status: 200 }
      else
        { error: user.validation_error, status: 500 }
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
      { error: user.validation_error, status: 500 }
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
        { error: user.validation_error, status: 500 }
      end
    else
      if user.regenerate_auth_token
        { user: user.sign_in_format, status: 200 }
      else
        { error: user.validation_error, status: 500 }
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
        { error: user.validation_error.present? ? user.validation_error : "Please verify your email first.", status: 500 }
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
        { error: user.validation_error, status: 500 }
      end
    else
      { error: "Invalid token.", status: 500 }
    end
  end

  def self.change_password(user, password)
    if user.update(password: password)
      { status: 200 }
    else
      { error: user.validation_error, status: 500 }
    end
  end

  def self.sign_out(user)
    if user.regenerate_auth_token
      { status: 200 }
    else
      render json: { error: user.validation_error, status: 500 }
    end
  end

  def self.show_data(user)
    { user: user.show_format, status: 200 }
  end

  def self.save_data(user, data, photo)
    data.merge!({ photo: photo }) if photo.present?

    if user.update(data)
      { user: user.show_format, status: 200 }
    else
      { error: user.validation_error, status: 500 }
    end
  end

  def self.delete_account(user)
    if user.destroy
      { status: 200 }
    else
      { error: user.validation_error, status: 500 }
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

  validate :account_type_validity
  validate :default_storage_type_validity
  validate :mode_type_validity

  # callbacks
  before_save :encrypt_password, if: -> { (is_create? && is_email_account?) || (!is_create? && is_email_account? && is_password_changed?) }

  # instance methods
  def sign_up_format
    self.as_json(only: [:id, :first_name, :last_name, :email], methods: [:phone, :photo_url])
  end

  def sign_in_format
    self.as_json(only: [:id, :first_name, :last_name, :auth_token], methods: [:phone, :photo_url]).merge({ email: self.email.to_s })
  end

  def show_format
    self.as_json(only: [:id, :email], methods: [:phone, :photo_url, :name])
  end

  def photo_url
    self.photo.to_s
  end

  def phone
    self.phone_number.to_s
  end

  def name
    self.first_name.to_s + " " + self.last_name.to_s
  end

  def validation_error
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

  def update_data(data)
    if self.update(data)
      { status: 200 }
    else
      { error: self.validation_error, status: 500 }
    end
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

    def account_type_validity
      if ![0,1,2,3].include?(self.account_type)
        errors.add(:account_type, "is not supported")
      end
    end

    def default_storage_type_validity
      if ![0,1,2].include?(self.default_storage_type)
        errors.add(:default_storage_type, "is not supported")
      end
    end

    def mode_type_validity
      if ![0,1,2].include?(self.mode_type)
        errors.add(:mode_type, "is not supported")
      end
    end

    def encrypt_password
      self.password = Password.create(self.password)
    end
end
