class UserMailer < ApplicationMailer
  default from: Rails.application.credentials.dig(:mailer, :email)

  def verification_email
    @user = params[:user]
    @token = @user.verification_token

    mail(to: @user.email, subject: 'CopWatch Email Verification')
  end

  def forgot_password_email
    @user = params[:user]
    @link = app_forgot_password_passwords_url({ token: @user.password_reset_token })

    mail(to: @user.email, subject: 'Forgot Password')
  end
end