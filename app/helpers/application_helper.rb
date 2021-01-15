module ApplicationHelper
  def theme_stylesheets
    case controller_name
    when 'sessions'
      "admin/shared/stylesheets/theme"
    end
  end

  def page_stylesheets
    logger.info "\n-- page_stylesheets --\ncontroller_name: #{controller_name}"
    case controller_name
    when 'sessions'
      stylesheet_link_tag 'authentication'
    else
      stylesheet_link_tag 'dataTables.bootstrap4.min'
    end
  end

  def page_js
    if controller_name == 'users'
      javascript_pack_tag "admin/#{controller_name}"
    end
  end

  def body_class
    case controller_name
    when 'sessions'
      body_page_class
    end
  end

  def body_page_class
    case controller_name
    when 'sessions'
      'authentication'
    end
  end

  def flash_message_class(type)
    case type
    when 'notice'
      flash[:error].present? ? '' : 'hidden'
    when 'alert'
      flash[:alert].present? ? 'alert-danger' : 'hidden'
    end
  end

  def admin_model
    @admin = current_admin || Admin.new
  end

  def page_name
    controller_name.capitalize
  end

  def page_action
    case action_name
    when 'index'
      'All'
    when 'show'
      if controller_name == 'users'
        'Profile'
      else
        'Details'
      end
    when 'edit'
      'Edit'
    else
      ''
    end
  end

  def content_header
    case action_name
    when 'index'
      "#{page_action} #{page_name}"
    when 'show'
      if controller_name = "users"
        "User Profile"
      else
        "#{controller_name.singularize} Details"
      end
    when 'edit'
      "Edit #{page_name.singularize}"
    end
  end

  def verified_class(user)
    user.is_verified ? 'badge-success' : 'badge-default'
  end

  def editable?
    case action_name
    when 'show'
      true
    when 'edit'
      false
    end
  end

  def photo_upload_display_class
    if action_name == 'show'
      'hide'
    else
      ''
    end
  end

  def form_buttons_visibility(type)
    case type
    when 'submit'
      if action_name == 'show'
        'hide'
      end
    when 'edit'
      if action_name == 'edit'
        'hide'
      end
    end
  end

  def form_back_btn_url
    if action_name == 'show'
      admin_users_path
    elsif 'edit'
      admin_user_path(@user)
    end
  end

  def form_back_btn_text
    if action_name == 'show'
      'Back'
    elsif 'edit'
      'Cancel'
    end
  end

  def admin_data(type)
    case type
    when 'id'
      current_admin.id
    when 'email'
      current_admin.email
    when 'name'
      current_admin.name.titleize
    when 'photo'
      current_admin.photo_url
    end
  end

  def user_data(user, type)
    case type
    when 'id'
      user.id
    when 'email'
      user.email.to_s
    when 'name'
      user.name.titleize
    when 'phone'
      user.phone
    when 'photo'
      user.photo_url
    when 'verified'
      user.is_verified ? 'Yes' : 'No'
    when 'account_type'
      user.account_type_name
    when 'mode_type'
      user.mode_type_name
    when 'joined_date'
      user.joined_date
    end
  end
end
