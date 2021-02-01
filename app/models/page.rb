class Page < ApplicationRecord
  # page_type:
    # 1 - about us
    # 2 - privacy policy
    # 3 - terms & conditions
    # 4 - legal disclaimer

  # associations

  # scopes
  scope :about_us,          -> { find_by(page_type: 1) }
  scope :privacy_policy,    -> { find_by(page_type: 2) }
  scope :terms_conditions,  -> { find_by(page_type: 3) }
  scope :legal_disclaimer,  -> { find_by(page_type: 4) }

  # class methods
  def self.seed
    page_types  = Page.select(:page_type).pluck(:page_type)

    Page.transaction do
      [
        { title: "About Us", page_type: 1 }, { title: "Privacy Policy", page_type: 2 },
        { title: "Terms & Conditions", page_type: 3 }, { title: "Legal Disclaimer", page_type: 4 }
      ].each do |page|
        Page.create!(page) if !page_types.include?(page[:page_type])
      end
    end
  end

  def self.formatted
    includes(:category)
      .as_json(only: [:id, :title, :content], methods: [:category_name])
      .each { |cat| cat["content"] = cat["content"].to_s if cat["content"].nil?}
  end

  def self.init(page_type, categories)
    case page_type
    when 1
      category = Category.find_by()
      # Pagen.new({ category_id: })
    when 2
    when 3
    when 4
    end
  end

  # validations

  # callbacks

  # instance methods
  def category_name
    self.category.name
  end
end
