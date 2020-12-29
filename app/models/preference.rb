class Preference < ApplicationRecord
  # associations
  belongs_to :user

  # scopes

  # class methods
  def self.save_data(data)
    preference = Preference.find_by(user_id: data[:user_id])

    if preference.nil?
      preference = Preference.new(data)

      if preference.save
        Preference.success_response({ preference: preference.save_data_format })
      else
        Preference.error_response(preference.validation_error)
      end
    else
      if preference.update(data.except(:user_id))
        Preference.success_response({ preference: preference.save_data_format })
      else
        Preference.error_response(preference.validation_error)
      end
    end
  end

  def self.show_data(user_id)
    preference = Preference.find_by(user_id: user_id)

    if preference.present?
      Preference.success_response({ preference: preference.save_data_format })
    else
      Preference.error_message("Preference not found.", 404)
    end
  end

  def self.success_response(data={})
    if data.class.name == "Hash"
      data.merge({ status: 200 })
    else
      { status: 200 }
    end
  end

  def self.error_response(error="", status = 500)
    { error: error, status: status }
  end

  # validations

  # callbacks

  # instance methods
  def save_data_format
    self.as_json(except: [:id, :created_at, :updated_at])
  end

  def validation_error
    self.errors.full_messages.first
  end
end
