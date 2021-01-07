class Admin < ApplicationRecord
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable, :trackable and :omniauthable, :registerable
  # :recoverable, :rememberable, :validatable
  devise :database_authenticatable

  # association

  # scopes

  # class methods
  def self.create_test_admin
    Admin.create!({ email: "admin@copwatch.com", password: "password", first_name: "test", last_name: "admin" })
  end

  # validations

  # callbacks

  # instance methods
end
