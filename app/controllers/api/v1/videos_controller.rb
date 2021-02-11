class Api::V1::VideosController < ApiController
  before_action :authenticate_user

  def index
    render json: Video.for_index(@current_user.id, params[:parent_video_id])
  end

  def create
    render json: Video.save_data(video_params, params[:is_last_part])
  end

  private

    def video_params
      params.require(:video).permit(:user_id, :title, :link, :parent_video_id)
    end
end