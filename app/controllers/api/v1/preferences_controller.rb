class Api::V1::PreferencesController < ApiController
  before_action :authenticate_user

  def show
    render json: Preference.show_data(@current_user.id)

  rescue StandardError => err
    logger.info "\n-- Preferences : Controller : show --\nError: #{err}\n"
    render json: { error: "Failed to retrieve preferences. Please try again later.", status: 500 }
  end

  def create
    render json: Preference.save_data(preference_params)

  rescue StandardError => err
    logger.info "\n-- Preferences : Controller : create --\nError: #{err}\n"
    render json: { error: "Failed to create preferences. Please try again later.", status: 500 }
  end

  def update
    render json: Preference.save_data(preference_params)

  rescue StandardError => err
    logger.info "\n-- Preferences : Controller : update --\nError: #{err}\n"
    render json: { error: "Failed to update preferences. Please try again later.", status: 500 }
  end

  private

    def preference_params
      params.require(:preference)
        .permit(
          :user_id, :is_recording_to_cloud, :is_dim_my_screen, :is_do_not_disturb,
          :is_recording_audio_video, :is_voice_activated, :is_rear_camera_selected
        )
    end
end