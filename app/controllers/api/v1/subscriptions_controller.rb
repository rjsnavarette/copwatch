class Api::V1::SubscriptionsController < ApiController
  before_action :authenticate_user

  def create
    render json: Subscription.save_data(@current_user, subscription_params)
  end

  private

    def subscription_params
      params.require(:subscription).permit(:user_id, :itemable_id, :itemable_type)
    end
end