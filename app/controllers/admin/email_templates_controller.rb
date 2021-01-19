class Admin::EmailTemplatesController < ApplicationController
  before_action :authenticate_admin!

  def index
    @email_templates = EmailTemplate.includes(:category).all
  end
end