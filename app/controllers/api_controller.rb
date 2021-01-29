class ApiController < ActionController::Base
  skip_before_action  :verify_authenticity_token

  def authenticate_user
    @current_user = params[:auth_token].present? ? User.find_by(auth_token: params[:auth_token]) : nil

    if @current_user.nil?
      render json: { error: "Unauthorized! Please sign-in", status: 401 }
    elsif !@current_user.is_verified
      render json: { error: "Unauthorized! Please verify your email to continue.", status: 401 }
    end
  end
end