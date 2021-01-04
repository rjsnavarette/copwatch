class Api::V1::PermissionsController < ApiController
  before_action :authenticate_user

  def show
    render json: Permission.show_data(@current_user.id)

  rescue StandardError => err
    logger.info "\n-- Permission : Controller : show --\nError: #{err}\n"
    render json: { error: "Failed to retrieve permissions. Please try again later.", status: 500 }
  end

  def create
    render json: Permission.save_data(permission_params)

  rescue StandardError => err
    logger.info "\n-- Permission : Controller : create --\nError: #{err}\n"
    render json: { error: "Failed to create permissions. Please try again later.", status: 500 }
  end

  def update
    render json: Permission.save_data(permission_params)

  rescue StandardError => err
    logger.info "\n-- Permission : Controller : update --\nError: #{err}\n"
    render json: { error: "Failed to update permissions. Please try again later.", status: 500 }
  end

  private

    def permission_params
      params.require(:permission)
        .permit(
          :has_camera, :has_cloud_communication, :has_phone_lock_dnd_screen_dim,
          :has_siri_bixby_google_interface_command, :has_advance_agreed, :user_id
        )
    end
end