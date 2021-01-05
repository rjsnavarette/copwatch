class Api::V1::RegistrationsController < ApiController
  def create
    render json: User.sign_up(user_params, params[:photo])

  rescue StandardError => err
    logger.info "\n-- Registration : Controller : create --\nError: #{err}\n"
    render json: { error: "Registration Failed. Please try again later.", status: 500 }
  end

  private

    def user_params
      params.require(:user).permit(:email, :password, :first_name, :last_name, :phone_number, :photo)
    end
end