class Api::V1::UsersController < ApiController
  before_action :authenticate_user, except: [:verify]

  def show
    render json: User.show_data(@current_user)

  rescue StandardError => err
    logger.info "\n-- Users : Controller : show --\nError: #{err}\n"
    render json: { error: "Failed to retrieve user profile. Please try again later.", status: 500 }
  end

  def verify
    render json: User.verify_email(params[:email], params[:token])

  rescue StandardError => err
    logger.info "\n-- Users : Controller : verify --\nError: #{err}\n"
    render json: { error: "Failed to verify. Please try again later.", status: 500 }
  end

  def select_default_storage
    render json: @current_user.update_data(default_storage_params)

  rescue StandardError => err
    logger.info "\n-- Users : Controller : select_default_storage --\nError: #{err}\n"
    render json: { error: "Failed to update selected default storage. Please try again later.", status: 500 }
  end

  def select_mode
    render json: @current_user.update_data(mode_params)

  rescue StandardError => err
    logger.info "\n-- Users : Controller : select_mode --\nError: #{err}\n"
    render json: { error: "Failed to update selected mode. Please try again later.", status: 500 }
  end

  private

    def default_storage_params
      params.require(:user).permit(:default_storage_type)
    end

    def mode_params
      params.require(:user).permit(:mode_type)
    end
end