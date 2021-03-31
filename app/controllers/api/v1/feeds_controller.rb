class Api::V1::FeedsController < ApiController
  before_action :authenticate_user

  def create
    render json: Feed.save_data(feed_params, params[:image])

  rescue StandardError => err
    logger.info "\n-- Feed : Controller : create --\nError: #{err}\n"
    render json: { error: "Failed to create feedback. Please try again later.", status: 500 }
  end

  private

    def feed_params
      params.require(:feed).permit(:user_id, :feed, :image)
    end
end
