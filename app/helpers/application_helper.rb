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
      render "admin/shared/stylesheets/pages/sessions"
    else
      ""
    end
  end

  def body_class
    case controller_name
    when 'sessions'
      "theme-blush #{body_page_class}"
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
    @admin = @admin || Admin.new
  end
end
