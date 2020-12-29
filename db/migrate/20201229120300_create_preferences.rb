class CreatePreferences < ActiveRecord::Migration[6.0]
  def change
    create_table :preferences do |t|
      t.integer :user_id
      t.boolean :is_recording_to_cloud, default: true
      t.boolean :is_dim_my_screen, default: true
      t.boolean :is_do_not_disturb, default: true
      t.boolean :is_recording_audio_video, default: true
      t.boolean :is_voice_activated, default: true
      t.boolean :is_rear_camera_selected, default: true

      t.timestamps
    end
    add_index :preferences, :user_id
  end
end
