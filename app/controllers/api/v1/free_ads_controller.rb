class Api::V1::FreeAdsController < ApiController
  before_action :authenticate_user

  def index
    render json: FreeAd.for_index
  end
end