class CreatePermissions < ActiveRecord::Migration[6.0]
  def change
    create_table :permissions do |t|
      t.integer :user_id
      t.boolean :has_camera, default: false
      t.boolean :has_cloud_communication, default: false
      t.boolean :has_phone_lock_dnd_screen_dim, default: false
      t.boolean :has_siri_bixby_google_interface_command, default: false
      t.boolean :has_advance_agreed, default: false

      t.timestamps
    end
    add_index :permissions, :has_camera
    add_index :permissions, :has_cloud_communication
    add_index :permissions, :has_phone_lock_dnd_screen_dim
    add_index :permissions, :has_siri_bixby_google_interface_command
  end
end
