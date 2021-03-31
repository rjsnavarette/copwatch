class Api::V1::PostFeedsController < ApiController
  before_action :authenticate_user

  def create
    render json: PostFeeds.save_data(feedback_params, params[:image])

  rescue StandardError => err
    logger.info "\n-- Feedback : Controller : create --\nError: #{err}\n"
    render json: { error: "Failed to create feedback. Please try again later.", status: 500 }
  end

  private

    def feedback_params
      params.require(:feedback).permit(:user_id, :description, :image)
    end
end
