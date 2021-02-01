class Category < ApplicationRecord
  # category_type:
    # 0 - none
    # 1 - email

  # sub_type:
    # 0 - default / none
    # 1 - verification email
    # 2 - forgot password

  # associations
  has_many :email_templates
  has_many :pages

  # scopes
  scope :email, -> { find_by(category_type: 1) }

  # class methods
  def self.seed
    category_names = Category.select(:name).pluck(:name)
    Category.transaction do
      [
        { name: "Email Verification", sub_type: 1, category_type: 1 },
        { name: "Forgot Password", sub_type: 2, category_type: 1 }
      ].each do |data|
          Category.create!(data) if !category_names.include?(data[:name])
        end
    end
  end

  # validations
  validates :name, presence: true

  # callbacks

  # instance methods
  def type_name
    ["", "Email Template"][self.category_type]
  end

  def sub_type_name
    ["", "Email Verification", "Forgot Password"][self.sub_type]
  end
end
