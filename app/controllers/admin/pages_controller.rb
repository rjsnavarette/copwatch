class Admin::PagesController < ApplicationController
  before_action :authenticate_admin!
  before_action :get_page

  def create
  end

  def update
    if @page.update(page_params)
      flash[:success] = "#{@page.title} updated!"
    else
      flash[:error] = @page.validation_error
    end

    redirect_to page_url

  rescue StandardError => err
    logger.info "\n-- Pages : Controller : update : Error --\n#{err}\n"
    flash[:error] = "An error occured while updating. Please try again later."
    redirect_to page_url
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
      params.require(:page).permit(:title, :content)
    end

    def get_page
      if params[:id].nil?
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
      else
        @page = Page.find_by(id: params[:id])
      end
    end

    def page_url
      case @page.name
      when 'about_us'
        about_us_admin_pages_path
      when 'privacy_policy'
        privacy_policy_admin_pages_path
      when 'terms_conditions'
        terms_conditions_admin_pages_path
      when 'legal_disclaimer'
        legal_disclaimer_admin_pages_path
      end
    end

end