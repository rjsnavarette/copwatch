class Feedback < ApplicationRecord
  mount_uploader :image, FeedbackImageUploader

  # associations
  belongs_to :user

  # scopes

  # class methods
  def self.seed
    users_id = User.select(:id).pluck(:id)

    Feedback.transaction do
      100.times do
        Feedback.create!({
          user_id: users_id.shuffle!.first,
          description: FFaker::Lorem.words(rand(4..8)).join(" ").capitalize
        })
      end
    end
  end
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

  def image_url
    self.image.url.to_s
  end
end
