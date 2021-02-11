class Api::V1::NotificationsController < ApiController
  before_action :authenticate_user

  def index
    render json: Notification.for_index(@current_user.id)
  end
end