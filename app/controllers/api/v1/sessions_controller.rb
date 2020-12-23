class Api::V1::SessionsController < ApiController
  def create
    render json: User.sign_in(session_params)

  rescue StandardError => err
    logger.info "\n-- Sessions : Controller : create --\nError: #{err}\n"
    render json: { error: "Failed to sign-in. Please try again later.", status: 500 }
  end

  private

    def session_params
      params.require(:session).permit(:email, :password, :account_type, :uuid, :first_name, :last_name)
    end
end