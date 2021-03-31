class Admin < ApplicationRecord
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable, :trackable and :omniauthable, :registerable
  # :recoverable, :rememberable, :validatable
  devise :database_authenticatable

  mount_uploader :photo, UserPhotoUploader

  # association

  # scopes

  # class methods
  def self.create_test_admin
    Admin.create!({ email: "test.admin@copwatch.com", password: "password", first_name: "test", last_name: "admin" })
  end

  def self.seed
    Admin.create!({ email: "test.admin@copwatch.com", password: "password", first_name: "test", last_name: "admin" })
  end

  # validations

  # callbacks

  # instance methods
  def name
    "#{self.first_name} #{self.last_name}"
  end

  def photo_url
    self.photo.url.to_s
  end
end
