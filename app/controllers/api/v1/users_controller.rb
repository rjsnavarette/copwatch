class Api::V1::UsersController < ApiController
  def verify
    render json: User.verify_email(params[:email], params[:token])
  end
end