class PushNotification
  require 'fcm'

  def self.client
    FCM.new(Rails.application.credentials.dig(:firebase, :server_key))
  end

  def send(device_tokens, data)
    begin
      fcm       = PushNotification.client
      options   = {
        "notification": {
          "title": data[:title],
          "body": data[:body]
        }
      }

      response  = fcm.send(device_tokens, options)
    rescue StandardError => err
      logger.info "\n-- PushNotification : Error --\n#{err}"
    end
  end
end