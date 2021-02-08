class FreeAd < ApplicationRecord
  # Notes:
    # currency
      # 0 - $ US dollar
    # duration
      # 0 - unlimited
    # duration_type:
      # 0 - days
      # 1 - weeks
      # 2 - months

  # associations
  has_many :subscriptions, as: :itemable

  # scopes
  scope :active,      -> { where(is_active: true) }
  scope :disabled,    -> { where(is_active: false) }
  scope :unlimited,   -> { where(duration: 0) }
  scope :days,        -> { where(duration_type: 0) }
  scope :weeks,       -> { where(duration_type: 1) }
  scope :months,      -> { where(duration_type: 3) }
  scope :order_asc_price, -> { order(:price) }
  scope :order_desc_price, -> { order_asc_price.reverse }
  scope :order_asc_duration, -> { order(:duration, :duration_type) }
  scope :order_desc_duration, -> { order_asc_duration.reverse }

  # class methods
  def self.seed
    free_ad = nil
    response = { success: true }

    FreeAd.transaction do
      [{ duration: 1, price: 0.99 }, { duration: 12, price: 10.0 }].each do |data|
        free_ad = FreeAd.new(data)

        raise free_ad.validation_error unless free_ad.save
      end
    end

    response

  rescue StandardError => err
    response = { success: false, error: free_ad.validation_error.present? ? free_ad.validation_error : err }
  end

  def self.is_duplicate?(price, duration, duration_type, currency, free_ads=[])
    free_ads = FreeAd.select(:price, :duration, :duration_type, :currency).to_a if !free_ads.present?

    free_ads.find { |free_ad| free_ad.price == price && free_ad.duration == duration && free_ad.duration_type == duration_type && free_ad.currency == currency }.present?
  end

  def self.for_index(filter={})
    { free_ads: FreeAd.active.order_asc_duration.map(&:index_format), status: 200 }

  rescue StandardError => err
    puts "\n-- FreeAd : Model : for_index : Error --\n#{err}\n"
    logger.info "\n-- FreeAd : Model : for_index : Error --\n#{err}\n"
    { error: "Failed to retrieve FreeAd list. Please try again later.", status: 500 }
  end

  # validations
  validate :data_valid?

  # callbacks
  before_create :set_default_name

  # instance methods
  def length
    "#{self.duration} #{self.duration_type_name.pluralize(self.duration)}"
  end

  def duration_type_name
    ['day', 'week', 'month'][self.duration_type]
  end

  def itemable_type
    "FreeAd"
  end

  def formatted_price
    case currency
    when 0
      "$#{self.price}"
    end
  end

  def validation_error
    self.errors.full_messages.first.to_s
  end

  def index_format
    self.as_json(only: [:id, :name, :price, :currency], methods: [:length, :itemable_type])
  end

  private

    # custom validation methods
    def data_valid?
      begin
        if [nil, ""].include?(self.duration)
          errors.add(:duration, "can't be empty")
        elsif self.duration < 0
          errors.add(:duration, "must be greater or equal to 0, where 0 is unlimited")
        elsif [nil, ""].include?(self.duration_type)
          errors.add(:duration_type, "can't be empty")
        elsif self.duration_type < 0 || self.duration_type > 3
          errors.add(:duration_type, "must either be days (0) or weeks (1) or months (3)")
        elsif self.price < 0
          errors.add(:price, "must be greater or equal to 0")
        elsif FreeAd.is_duplicate?(self.price, self.duration, self.duration_type, self.currency)
          errors.add(:free_ad, 'already exists')
        end
      rescue StandarError => err
        puts "\n-- FreeAd : Model : data_valid? : Error --\n#{err}\n"
        logger.info "\n-- FreeAd : Model : data_valid? : Error --\n#{err}\n"
        raise "Failed to validate FreeAd data."
      end
    end

    # callback methods
    def set_default_name
      begin
        self.name = self.length if self.name.nil? || self.name.empty?
      rescue StandarError => err
        puts "\n-- FreeAd : Model : set_default_name : Error --\n#{err}\n"
        logger.info "\n-- FreeAd : Model : set_default_name : Error --\n#{err}\n"
        raise "Failed to set FreeAd default name."
      end
    end
end