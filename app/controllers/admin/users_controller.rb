class Admin::UsersController < ApplicationController
  before_action :authenticate_admin!
  before_action :get_user, except: [:index]

  def index
    @users = User.select(:id, :first_name, :last_name, :email, :phone_number, :is_verified, :account_type, :mode_type, :created_at).order(:id).all
  end

  def show
  end

  def edit
    render :show
  end

  def update
    if @user.update(user_params)
      redirect_to admin_user_path(@user)
    else
      render :edit
    end
  end

  def destroy
    if @user.destroy
      flash[:success] = "User deleted!"
    else
      flash[:error] = "Failed to delete user. Please try again later."
    end

    redirect_to admin_users_path
  end

  private

    def get_user
      @user = User.find_by(id: params[:id])

      if @user.nil?
        render :file => "#{Rails.root}/public/404.html",  layout: false, status: :not_found
      end
    end

    def user_params
      params.require(:user).permit(:first_name, :last_name, :phone_number, :photo)
    end

end
