class Video < ApplicationRecord
  # associations
  belongs_to  :user
  has_one     :parent_video, class_name: 'Video', foreign_key: :parent_video_id
  has_one     :notification, as: :notifiable, dependent: :destroy

  # scope
  scope :for_user,  -> (user_id) { where(user_id: user_id).order("id, sequence_index") }
  scope :parts,     -> (parent_id) { where(id: parent_id).or(where(parent_id: parent_id)).order("id, sequence_index") }

  # class methods
  def self.for_index(user_id, video_parent_id)
    videos = Video.for_user(user_id)

    videos = videos.parts(video_parent_id) if video_parent_id.present?

    { videos: videos, status: 200 }

  rescue StandardError => err
    puts        "\n-- Video : Model : for_index : Error --\n#{err}\n"
    logger.info "\n-- Video : Model : for_index : Error --\n#{err}\n"
    { error: "An error occured while getting the videos. Please try again later.", status: 500 }
  end

  def self.save_data(data, is_last_part=false)
    video = Video.new(data)

    if video.save
      notification = Notification.save_data(video.user_id, video)

      if notification && (video.parent_video_id.nil? || [true, 'true'].include?(is_last_part))
        PushNotification.send([video.user.device_token], { title: notification.title, body: notification.description })
      end

      { parent_video_id: video.parent_video_id || video.id, status: 200 }
    else
      { error: video.validation_error, status: 500 }
    end

  rescue StandardError => err
    puts        "\n-- Video : Model : save_data : Error --\n#{err}\n"
    logger.info "\n-- Video : Model : save_data : Error --\n#{err}\n"
    { error: video.validation_error.present? ? video.validation_error : "An error occured while saving video. Please try again later.", status: 500 }
  end

  def self.index_format
    videos = []

    each do |video|
      title         = video.title.split("_").first(2).join("_")
      existing_vid  = videos.find { |vid| vid[:title] == title }
      formatted_vid = video.index_format

      if existing_vid.present?
        existing_vid[:data] << formatted_vid
      else
        videos << { title: title, parent_id: video.id, data: [formatted_vid]}
      end
    end

    { videos: videos, status: 200 }

  rescue StandardError => err
    puts        "\n-- Video : Model : self.index_format : Error --\n#{err}\n"
    logger.info "\n-- Video : Model : self.index_format : Error --\n#{err}\n"
    { error: "An error occured while getting the videos. Please try again later.", status: 500 }
  end

  # validations
  validates :title, presence: true
  validates :link, presence: true

  # callbacks
  before_create :set_sequence_index

  # instance methods
  def validation_error
    self.errors.full_messages.first.to_s
  end

  def index_format
    self.as_json(except: [:user_id, :created_at, :updated_at])
  end

  private

    def set_sequence_index
      if self.parent_video_id.nil?
        self.sequence_index = 0
      else
        self.sequence_index = Video.where(parent_video_id: self.parent_video_id).size + 1
      end
    end
end
