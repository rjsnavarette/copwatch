class Feedback < ApplicationRecord
  mount_uploader :image, FeedbackImageUploader

  # associations
  belongs_to :user

  # scopes

  # class methods
  def self.save_data(data, image)
    feedback        = Feedback.new(data)
    feedback.image  = image if image.present?

    if feedback.save
      { status: 200 }
    else
      { error: feedback.validation_error, status: 500 }
    end
  end

  # validations
  validates :description, presence: true

  # callbacks

  # instance methods
  def validation_error
    self.errors.full_messages.first
  end
end
