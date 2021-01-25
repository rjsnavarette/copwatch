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

  def menu_class(name)
    controller_name == name ? 'active open' : ''
  end

  def menu_toggled(name)
    controller_name == name ? 'toggled' : ''
  end

  def sub_menu_toggled_class(controller, action)
    controller_name == controller && action_name == action ? 'toggled' : ''
  end

  def sub_menu_active_class(menu, submenu)
    controller_name == menu && action_name == submenu ? 'active' : ''
  end

  def add_btn_visibility_class
    if controller_name != 'email_templates' || (controller_name == 'email_templates' && action_name == 'new')
      'hide'
    end
  end

  def admin_model
    @admin = current_admin || Admin.new
  end

  def page_name
    controller_name.split('_').join(' ').titleize
  end

  def page_action
    case action_name
    when 'index'
      'All'
    when 'new'
      action_name.capitalize
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
      if controller_name == "users"
        "User Profile"
      else
        "#{controller_name.split("_").join(" ").titleize.singularize} Details"
      end
    when 'edit', 'new'
      "#{action_name.capitalize} #{page_name.singularize}"
    end
  end

  def verified_class(user)
    user.is_verified ? 'badge-success' : 'badge-default'
  end

  def editable?
    case action_name
    when 'edit'
      false
    else
      true
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
      if ['new', 'edit'].include?(action_name)
        'hide'
      end
    end
  end

  def form_back_btn_text
    case action_name
    when 'show'
      'Back'
    when 'edit', 'new'
      'Cancel'
    else
      ''
    end
  end

  def form_action_btn_url(type, model_instance=nil)
    case controller_name
    when 'users'
      if action_name == 'show' && type == 'edit'
        edit_admin_user_path(model_instance)
      elsif action_name == 'show' && type == 'back'
        admin_users_path
      elsif action_name == 'edit' && 'back'
        admin_user_path(model_instance)
      else
        ""
      end
    when 'email_templates'
      if action_name == 'show' && type == 'back' || action_name == 'new' && type == 'back'
        admin_email_templates_path
      elsif action_name == 'show' && type == 'edit'
        edit_admin_email_template_path(model_instance)
      elsif action_name == 'edit' && type == 'back'
        admin_email_template_path(model_instance)
      else
        ""
      end
    end
  end

  def form_url(model_instance)
    case controller_name
    when 'users'
      case action_name
      when 'new'
        admin_users_path(model_instance)
      when 'show', 'edit'
        admin_user_path(model_instance)
      end
    when 'email_templates'
      case action_name
      when 'new'
        admin_email_templates_path(model_instance)
      when 'show', 'edit'
        admin_email_template_path(model_instance)
      end
    when 'pages'
      edit_admin_page_path(model_instance)
    end
  end

  def form_method
    case action_name
    when 'new'
      :post
    else
      :put
    end
  end

  def for_actions_visibility
    case controller_name
    when 'pages'
      if ['about_us', 'privacy_policy', 'terms_conditions', 'legal_disclaimer'].include?(action_name)
        'hide'
      else
        ''
      end
    else
      ''
    end
  end

  def add_url
    case controller_name
    when 'email_templates'
      new_admin_email_template_path(@template)
    end
  end

  def pages_url(submenu)
    case submenu
    when 'about_us'
      about_us_admin_pages_path
    when 'privacy_policy'
      privacy_policy_admin_pages_path
    when 'terms_conditions'
      terms_conditions_admin_pages_path
    when 'legal_disclaimer'
      legal_disclaimer_admin_pages_path
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

  def feedback_data(feedback, type)
    case type
    when 'id'
      feedback.id
    when 'description'
      feedback.description.to_s
    when 'image'
      feedback.image_url
    when 'created'
      feedback.created_at.strftime("%b %-d, %Y")
    end
  end

  def template_data(template, type)
    case type
    when 'id'
      template.id
    when 'subject'
      template.subject
    when 'greetings'
      template.greetings
    when 'content'
      template.content
    when 'closing'
      template.closing
    end
  end

  def category_data(category, type)
    case type
    when 'id'
      category.id
    when 'name'
      category.name
    when 'type_name'
      category.type_name
    when 'sub_type_name'
      category.sub_type_name
    end
  end
end
