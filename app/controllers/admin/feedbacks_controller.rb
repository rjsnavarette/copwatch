class Admin::FeedbacksController < ApplicationController
  before_action :authenticate_admin!

  def index
    @feedbacks  = Feedback.includes(:user)
                    .select("feedbacks .*, users.id, users.first_name, users.last_name")
                    .references("users").all
  end

  def show
    @feedback   = Feedback.includes(:user).find_by(id: params[:id])
    @user_name  = @feedback.user.present? ? @feedback.user.name : ""
  end

  def destroy
    @feedback = Feedback.find_by(id: params[:id])

    if @feedback.present? && @feedback.destroy
      flash[:success] = "Feedback removed!"
    else
      flash[:error] = "Failed to remove feedback. Please try again later."
    end

    redirect_to admin_feedbacks_path
  end
end