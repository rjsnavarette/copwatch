class CreateNotifications < ActiveRecord::Migration[6.0]
  def change
    create_table :notifications do |t|
      t.integer :user_id
      t.integer :notifiable_id
      t.string :notifiable_type
      t.string :title
      t.string :description
      t.boolean :is_read, default: false

      t.timestamps
    end
    add_index :notifications, :user_id
    add_index :notifications, :notifiable_id
    add_index :notifications, :notifiable_type
    add_index :notifications, :is_read
  end
end
