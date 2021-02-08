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
    all.as_json(only: [:id, :title, :content])
  end

  def self.menu_format
    Page.select(:title, :page_type).all
  end

  # validations

  # callbacks

  # instance methods
  def name
    case self.page_type
    when 1
      'about_us'
    when 2
      'privacy_policy'
    when 3
      'terms_conditions'
    when 4
      'legal_disclaimer'
    end
  end

  def validation_error
    self.errors.full_messages.first.to_s
  end

  def api_format
    as_json(only: [:id, :title, :content])
  end
end
