class Admin::PagesController < ApplicationController
  before_action :authenticate_admin!
  before_action :get_page

  def edit
  end

  def create
  end

  def update
  end

  def destroy
  end

  def about_us
  end

  def privacy_policy
  end

  def terms_conditions
  end

  def legal_disclaimer
  end

  private

    def page_params
      params.require(:page).permit(:title, :content, :category_id)
    end

    def get_page
      case action_name
      when 'about_us'
        @page = Page.about_us
      when 'privacy_policy'
        @page = Page.privacy_policy
      when 'terms_conditions'
        @page = Page.terms_conditions
      when 'legal_disclaimer'
        @page = Page.legal_disclaimer
      end
    end

end