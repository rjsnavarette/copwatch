class UserMailer < ApplicationMailer
  def verification_email
    @user = params[:user]
    @link = "copwatch://open?action=0&code=#{@user.verification_code}"

    mail(to: @user.email, subject: 'CopWatch Email Verification')
  end

  def forgot_password_email
    @user = params[:user]
    @link = "copwatch://open?action=1&code=#{@user.password_reset_token}"

    mail(to: @user.email, subject: 'Forgot Password')
  end
end