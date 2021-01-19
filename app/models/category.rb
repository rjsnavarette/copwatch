class Category < ApplicationRecord
  # associations
  has_many :email_templates

  # scopes
  scope :email, -> { where(category_type: 1) }

  # class methods
  def self.seed
    # category_type: 0 - default / none, 1 - email
    # sub_type: 0 - default / none, 1 - verification email, 2 - forgot password

    Category.transaction do
      [{ name: "Email Verification", sub_type: 1 }, { name: "Forgot Password", sub_type: 2 }]
        .each do |data|
          data.merge!({ category_type: 1 })

          Category.create!(data)
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
