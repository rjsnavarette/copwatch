class Admin::FreeAdsController < ApplicationController
  before_action :authenticate_admin!

  def index
    @free_ads = FreeAd.all
  end
end