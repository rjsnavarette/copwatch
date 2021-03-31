class CreateFeeds < ActiveRecord::Migration[6.0]
  def change
    create_table :feeds do |t|
      t.text :feed
      t.text :comment
      t.text :reply
      t.string :like
      t.string :image
      t.integer :user_id

      t.timestamps
    end
    add_index :feeds, :user_id
  end
end
