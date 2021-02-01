class CreateSubscriptions < ActiveRecord::Migration[6.0]
  def change
    create_table :subscriptions do |t|
      t.integer :user_id
      t.integer :itemable_id
      t.string :itemable_type
      t.datetime :expiry_at

      t.timestamps
    end
    add_index :subscriptions, :user_id
    add_index :subscriptions, :itemable_id
    add_index :subscriptions, :itemable_type
    add_index :subscriptions, :expiry_at
  end
end
