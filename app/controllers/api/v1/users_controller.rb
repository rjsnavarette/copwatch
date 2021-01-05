class Api::V1::UsersController < ApiController
  before_action :authenticate_user, except: [:verify]

  def show
    render json: User.show_data(@current_user)

  rescue StandardError => err
    logger.info "\n-- Users : Controller : show --\nError: #{err}\n"
    render json: { error: "Failed to retrieve user profile. Please try again later.", status: 500 }
  end

  def update
    render json: User.save_data(@current_user, user_params, params[:photo])

  rescue StandardError => err
    logger.info "\n-- Users : Controller : update --\nError: #{err}\n"
    render json: { error: "Failed to update user. Please try again later.", status: 500 }
  end

  def destroy
    render json: User.delete_account(@current_user)

  rescue StandardError => err
    logger.info "\n-- Users : Controller : destroy --\nError: #{err}\n"
    render json: { error: "Failed to delete account. Please try again later.", status: 500 }
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

    def user_params
      params.require(:user).permit(:first_name, :last_name, :phone_number, :photo)
    end

    def default_storage_params
      params.require(:user).permit(:default_storage_type)
    end

    def mode_params
      params.require(:user).permit(:mode_type)
    end
end