class UserMailer < ApplicationMailer
  def verification_email
    @user = params[:user]

    mail(to: @user.email, subject: 'CopWatch Email Verification')
  end

  def forgot_password_email
    @user = params[:user]

    mail(to: @user.email, subject: 'Forgot Password')
  end
end