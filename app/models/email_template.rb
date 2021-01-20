class EmailTemplate < ApplicationRecord
  # associations
  belongs_to :category

  # scopes

  # class methods
  def self.seed_test
    if Category.count == 0
      Category.seed
    end

    EmailTemplate.transaction do
      Category.email.each do |category|
        EmailTemplate.create!({
          subject: category.name.titleize,
          greetings: FFaker::Lorem.words(rand(2..3)).join(" ").titleize,
          content: FFaker::Lorem.paragraphs(1).first,
          closing: FFaker::Lorem.words(2).join(" ").titleize,
          category_id: category.id
        })
      end
    end
  end

  # validations
  validates :subject, presence: true
  validates :greetings, presence: true
  validates :content, presence: true
  validates :closing, presence: true

  # callbacks

  # instance methods
  def validation_error
    self.errors.full_messages.first
  end
end
