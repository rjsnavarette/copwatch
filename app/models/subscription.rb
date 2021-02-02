class Subscription < ApplicationRecord
  # Notes

  # associations
  belongs_to :user
  belongs_to :itemable, polymorphic: true

  # scopes
  scope :active,   -> { where("expiry_at IS NULL or expiry_at > CURRENT_TIMESTAMP") }
  scope :history,  -> { where("expiry_at IS NOT NULL AND expiry_at <= CURRENT_TIMESTAMP") }
  scope :free_ad,  -> { active.find_by(itemable_type: 'FreeAd') }
  scope :free_ads, -> { where(itemable_type: 'FreeAd') }
  scope :for_user, -> (user_id) { where(user_id: user_id) }

  # class methods
  def self.save_data(current_user, data)
    subscription = Subscription.new(data)

    if subscription.user_id != current_user.id
      { error: "Invalid data. Subscriber data does not match your data.", status: 500 }
    elsif subscription.save
      { status: 200 }
    else
      { error: subscription.validation_error, status: 500 }
    end

  rescue StandardError => err
    puts "\n-- Subscription : Model : save_data : Error --\n#{err}\n"
    logger.info "\n-- Subscription : Model : save_data : Error --\n#{err}\n"
    { error: "Failed to subscribe. Please try again later.", status: 500 }
  end

  # validations
  validate :data_valid?

  # callbacks
  before_create :compute_expiry

  # instance methods
  def validation_error
    self.errors.full_messages.first.to_s
  end

  def formatted_expiry
    self.expiry_at.strftime("%Y-%m-%d %H:%M")
  end

  private

    # custom validation methods
    def data_valid?
      case self.itemable_type
      when 'FreeAd'
        logger.info "\n-- user id : #{self.user_id} --\n"
        if Subscription.for_user(self.user_id).active.free_ad.present?
          errors.add(:free_ad_subscription, "already exists and is active")
        end
      end
    end

    # callback methods
    def compute_expiry
      itemable = self.itemable

      if itemable.present?
        if self.itemable_type == 'FreeAd' && itemable.duration > 0
          case itemable.duration_type
          when 0
            #days
            self.expiry_at = DateTime.now.advance(days: itemable.duration)
          when 1
            #weeks
            self.expiry_at = DateTime.now.advance(weeks: itemable.duration)
          when 2
            #months
            self.expiry_at = DateTime.now.advance(months: itemable.duration)
          end
        end
      end
    end
end
