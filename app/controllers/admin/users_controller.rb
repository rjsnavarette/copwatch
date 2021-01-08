class Admin::UsersController < ApplicationController
  before_action :authenticate_admin!

  def index
    @users = User.select(:id, :first_name, :last_name, :email, :phone_number, :is_verified, :account_type, :mode_type, :created_at).order(:id).all
  end
end
