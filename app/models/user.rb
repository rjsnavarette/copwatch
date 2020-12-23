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
    user = User.new(data)

    User.transaction do
      if user.save && user.regenerate_auth_token && user.regenerate_auth_token 
        { user: user.registration_format, status: 200 }
      else
        { error: user.get_error, status: 500 }
      end
    end
  end

  # validations
  validates :email, presence: true, email: true, uniqueness: true
  validates :password, presence: true, length: { minimum: 8, maximum: 20 }, on: :create
  validates :first_name, presence: true
  validates :last_name, presence: true

  # callbacks
  before_commit :encrypt_password, on: :create

  # instance methods
  ## formatting methods
  def registration_format
    self.as_json(only: [:id, :first_name, :last_name, :email], methods: :photo_url)
  end

  def photo_url
    photo.to_s
  end

  ## helper methods
  def get_error
    self.errors.full_messages.first
  end

  private

    def encrypt_password
      logger.info "\n-- User : Model : encrypt_password --\n"
      self.password = Password.create(self.password)
    end
end
