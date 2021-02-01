class Permission < ApplicationRecord
  # associations
  belongs_to :user

  # scopes

  # class methods
  def self.save_data(data)
    permission = Permission.find_by(user_id: data[:user_id])

    if permission.nil?
      permission = Permission.new(data)

      if permission.save
        Permission.success_response({ preference: permission.save_data_format })
      else
        Permission.error_response(permission.validation_error)
      end
    else
      if permission.update(data.except(:user_id))
        Permission.success_response({ permission: permission.save_data_format })
      else
        Permission.error_response(permission.validation_error)
      end
    end
  end

  def self.show_data(user_id)
    permission = Permission.find_by(user_id: user_id)

    if permission.present?
      Permission.success_response({ permission: permission.save_data_format })
    else
      Permission.error_response("Permission not found.", 404)
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
