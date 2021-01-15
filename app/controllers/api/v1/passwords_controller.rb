class Api::V1::PasswordsController < ApiController
  before_action :authenticate_user, only: [:change]

  def forgot
    render json: User.forgot_password(params[:email])

  rescue StandardError => err
    logger.info "\n-- Password : Controller : forgot --\nError: #{err}\n"
    render json: { error: "Failed to process forgot password. Please try again later.", status: 500 }
  end

  def reset
    render json: User.reset_password(params[:token], params[:password])

  rescue StandardError => err
    logger.info "\n-- Password : Controller : reset --\nError: #{err}\n"
    render json: { error: "Failed to reset password. Please try again later.", status: 500 }
  end

  def change
    render json: User.change_password(@current_user, params[:password])

  rescue StandardError => err
    logger.info "\n-- Password : Controller : reset --\nError: #{err}\n"
    render json: { error: "Failed to reset password. Please try again later.", status: 500 }
  end
end