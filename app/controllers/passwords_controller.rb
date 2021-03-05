class PasswordsController < ApplicationController
  def app_forgot_password
    if User.select(:password_reset_token).find_by(password_reset_token: params[:token]).present?
      redirect_to "copwatch://open?action=1&token=#{params[:token]}"
    else
     render :file => "#{Rails.root}/public/404", :layout => false, :status => :not_found
    end
  end
end