class Notification < ApplicationRecord
  # associations
  belongs_to :user
  belongs_to :notifiable, polymorphic: :true

  # scopes
  scope :for_user,  -> (user_id) { where(user_id: user_id) }
  scope :for_video, -> (video_id) { where(notifiable_id: video_id, notifiable_type: "Video") }
  scope :videos,    -> { where(notifiable_type: "Video") }
  scope :read,      -> { where(is_read: true) }
  scope :unread,    -> { where(is_read: false) }

  # class methods
  def self.save_data(user_id, notifiable)
    notification = Notification.new({ user_id: user_id, notifiable: notifiable })

    case notifiable.class.name
    when "Video"
      notification.title        = "Video Upload"
      notification.description  = "Video Uploaded Successfully"
    end

    notification.save
  end

  def self.for_index(user_id)
    { notifications: for_user(user_id).videos.map(&:video_notifiable_format), status: 200 }

  rescue StandardError => err
    puts        "\n-- Api : V1 : Notifications : Controller : index : Error --\n#{err}\n"
    logger.info "\n-- Api : V1 : Notifications : Controller : index : Error --\n#{err}\n"

    { error: "An error occured while trying to get notifications. Please try again later.", status: 500 }
  end

  # validations

  # callbacks

  # instance methods
  def validation_error
    self.errors.full_messages.first.to_s
  end

  def video_notifiable_format
    self.as_json(only: [:title, :description, :is_read], methods: [:video_link])
  end

  def video_link
    if self.notifable_type == 'Video' && self.notifiable.present?
      self.notifiable.link.to_s
    else
      ""
    end
  end
end
