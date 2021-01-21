class Admin::PagesController < ApplicationController
  before_action :authenticate_admin!
  
  def about_us
  end

  def privacy_policy
  end

  def terms_conditions
  end

  def legal_disclaimer
  end
end