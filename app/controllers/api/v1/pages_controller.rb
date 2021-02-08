class Api::V1::PagesController < ApiController
  before_action :authenticate_user

  def about_us
    render json: { page: get_page.api_format, status: 200 }
  end

  def privacy_policy
    render json: { page: get_page.api_format, status: 200 }
  end

  def terms_conditions
    render json: { page: get_page.api_format, status: 200 }
  end

  def legal_disclaimer
    render json: { page: get_page.api_format, status: 200 }
  end

  private

    def get_page
      case action_name
      when 'about_us'
        Page.about_us
      when 'privacy_policy'
        Page.privacy_policy
      when 'terms_conditions'
        Page.terms_conditions
      when 'legal_disclaimer'
        Page.legal_disclaimer
      end
    end
end