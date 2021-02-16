class Admin::EmailTemplatesController < ApplicationController
  before_action :authenticate_admin!
  before_action :get_email_template, except: [:index, :create]

  def index
    @templates = EmailTemplate.includes(:category).all
  end

  def new
    render :show
  end

  def show
  end

  def edit
    render :show
  end

  def create
    @template = EmailTemplate.new(email_template_params)
    
    if @template.save
      redirect_to admin_email_template_path(@template)
    else
      flash[:error] = @template.validation_error
      redirect_to new_admin_email_template_path(@template)
    end
  end

  def update
    if @template.update(email_template_params)
      redirect_to admin_email_template_path(@template)
    else
      redirect_to edit_admin_email_template_path
    end
  end

  def destroy
    if @template.destroy
      flash[:success] = "Email Template deleted!"
    else
      flash[:error] = "Failed to delete user. Please try again later."
    end

    redirect_to admin_email_templates_path
  end

  private

    def email_template_params
      params.require(:email_template).permit(:subject, :greetings, :content, :closing, :category_id)
    end

    def get_email_template
      @categories = Category.all.collect { |cat| [cat.name, cat.id] }
      @template   = action_name != 'new' ? EmailTemplate.find_by(id: params[:id]) : EmailTemplate.new()

      if @template.nil?
        render :file => "#{Rails.root}/public/404.html",  layout: false, status: :not_found
      end
    end
end