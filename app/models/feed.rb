class Feed < ApplicationRecord
  #associations
  belongs_to :user

  #validations
  validates :feed, presence: true

  #class methods
  def self.seed
    users_id = User.select(:id).pluck(:id)

    Feed.transaction do
      100.times do
        Feed.create!({
          user_id: users_id.shuffle!.first,
          feed: FFaker::Lorem.words(rand(4..8)).join(" ").capitalize
        })
      end
    end
  end
end
